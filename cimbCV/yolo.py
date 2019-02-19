# original author: https://www.pyimagesearch.com/2018/11/12/yolo-object-detection-with-opencv/
# modified by: jeremyng123

# import the necessary packages
import numpy as np
import argparse
import time
import cv2
import os
import json


# import firebase_admin
# from firebase_admin import credentials
#
# # Firebase service account
# # firebase-adminsdk-cr8if@cimb2-53164.iam.gserviceaccount.com
# cred = credentials.Certificate("cimb2-53164-firebase-adminsdk-cr8if-1e7b30c67f.json")
# default_app = firebase_admin.initialize_app(cred)


# from firebase import firebase
# firebase = firebase.FirebaseApplication('https://cimb2-53164.firebaseio.com', None)
# result = firebase.get('/users', None)
# print(result)


# Initialize Firebase
# !pip install -q firebase-admin
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db
import time

print("[INFO] Initiating Google Firebase...\n")
cred = credentials.Certificate("cimb2-53164-firebase-adminsdk-cr8if-1e7b30c67f.json")
database_url = "https://cimb2-53164.firebaseio.com"
firebase_admin.initialize_app(cred, {'databaseURL': database_url})

print("[INFO] Login successful!")



# # As an admin, the app has access to read and write all data, regradless of Security Rules
# ref = db.reference('restricted_access/secret_document')
# print(ref.get())

"""FUCKING IMPORTANT DOCUMENTATION"""
# ref = db.reference('/jeremy')
# print(ref.get())
# ref.set(2)
# print(ref.get())
# ref.delete()





def img_read(image,target):
	# construct the argument parse and parse the arguments
	ap = argparse.ArgumentParser()
	ap.add_argument("-c", "--confidence", type=float, default=0.5,
		help="minimum probability to filter weak detections")
	ap.add_argument("-t", "--threshold", type=float, default=0.3,
		help="threshold when applying non-maxima suppression")
	args = vars(ap.parse_args())

	yolo = "yolo-coco"

	# load the COCO class labels our YOLO model was trained on
	labelsPath = os.path.sep.join([yolo, "coco.names"])
	LABELS = open(labelsPath).read().strip().split("\n")
	# print(LABELS)
	# print(len(LABELS))

	# initialize a list of colors to represent each possible class label
	np.random.seed(42)
	COLORS = np.random.randint(0, 255, size=(len(LABELS), 3),
		dtype="uint8")

	# derive the paths to the YOLO weights and model configuration
	weightsPath = os.path.sep.join([yolo, "yolov3.weights"])
	configPath = os.path.sep.join([yolo, "yolov3.cfg"])

	# load our YOLO object detector trained on COCO dataset (80 classes)
	print("[INFO] loading YOLO from disk...")
	net = cv2.dnn.readNetFromDarknet(configPath, weightsPath)

	# load our input image and grab its spatial dimensions
	image = cv2.imread(image)
	(H, W) = image.shape[:2]
	ratio = H * 0.002	# 0.002 is derived from attempting to scale down a picture of size 4500 by 5000 down to at least 450.
	H = H // ratio
	W = W // ratio
	print(f"H: {H} and W: {W}")

	# determine only the *output* layer names that we need from YOLO
	ln = net.getLayerNames()
	ln = [ln[i[0] - 1] for i in net.getUnconnectedOutLayers()]

	# construct a blob from the input image and then perform a forward
	# pass of the YOLO object detector, giving us our bounding boxes and
	# associated probabilities
	blob = cv2.dnn.blobFromImage(image, 1 / 255.0, (416, 416),
		swapRB=True, crop=False)
	net.setInput(blob)
	start = time.time()
	layerOutputs = net.forward(ln)
	end = time.time()

	# show timing information on YOLO
	print("[INFO] YOLO took {:.6f} seconds".format(end - start))

	# initialize our lists of detected bounding boxes, confidences, and
	# class IDs, respectively
	boxes = []
	confidences = []
	classIDs = []

	# loop over each of the layer outputs
	for output in layerOutputs:
		# loop over each of the detections
		for detection in output:
			# extract the class ID and confidence (i.e., probability) of
			# the current object detection
			scores = detection[5:]
			classID = np.argmax(scores)
			confidence = scores[classID]

			# filter out weak predictions by ensuring the detected
			# probability is greater than the minimum probability
			if confidence > args["confidence"]:
				# scale the bounding box coordinates back relative to the
				# size of the image, keeping in mind that YOLO actually
				# returns the center (x, y)-coordinates of the bounding
				# box followed by the boxes' width and height
				box = detection[0:4] * np.array([W, H, W, H])
				(centerX, centerY, width, height) = box.astype("int")

				# use the center (x, y)-coordinates to derive the top and
				# and left corner of the bounding box
				x = int(centerX - (width / 2))
				y = int(centerY - (height / 2))

				# update our list of bounding box coordinates, confidences,
				# and class IDs
				boxes.append([x, y, int(width), int(height)])
				confidences.append(float(confidence))
				classIDs.append(classID)

	# apply non-maxima suppression to suppress weak, overlapping bounding
	# boxes
	idxs = cv2.dnn.NMSBoxes(boxes, confidences, args["confidence"],
		args["threshold"])

	# ensure at least one detection exists
	if len(idxs) > 0:
		# loop over the indexes we are keeping
		for i in idxs.flatten():
			# extract the bounding box coordinates
			(x, y) = (boxes[i][0], boxes[i][1])
			(w, h) = (boxes[i][2], boxes[i][3])

			# draw a bounding box rectangle and label on the image
			color = [int(c) for c in COLORS[classIDs[i]]]
			cv2.rectangle(image, (x, y), (x + w, y + h), color, 2)
			text = "{}: {:.4f}".format(LABELS[classIDs[i]], confidences[i])

			# print(i)

			# if text = target label, then we can break the loop
			if (target == LABELS[classIDs[i]].lower()):
				cv2.putText(image, text, (x, y - 5), cv2.FONT_HERSHEY_SIMPLEX,
					0.5, color, 2)
				ref = db.reference('/Benjamin/result')
				ref.set(1)
				print(f"[INFO] {target} found in your picture!")
				return True
		ref = db.reference('/Benjamin/result')
		ref.set(0)
		print(f"[INFO] {target} cannot be found... please try again")
		return False
	ref = db.reference('/Benjamin/result')
	ref.set(0)
	print(f"[INFO] {target} cannot be found... please try again")
	return False

	# show the output image
	# cv2.imshow("Image", image)
	# cv2.waitKey(0)

# import rawpy as raw
# def convert_cr2_to_jpg(raw_image):
#     raw_image_process = raw.imread(raw_image)
#     buffered_image = numpy.array(raw_image_process.to_buffer())
#     if raw_image_process.metadata.orientation == 0:
#         jpg_image_height = raw_image_process.metadata.height
#         jpg_image_width = raw_image_process.metadata.width
#     else:
#         jpg_image_height = raw_image_process.metadata.width
#         jpg_image_width = raw_image_process.metadata.height
#     jpg_image = Image.frombytes('RGB', (jpg_image_width, jpg_image_height), buffered_image)
#     return jpg_image


# Convert URL to readable image
from PIL import Image
from io import BytesIO
import requests

# from skimage import io

from urllib.request import urlopen, Request
def download_image():
	
	ref = db.reference('/Benjamin/image')
	url_str = ref.get()
	length = len(url_str)
	url_str = url_str[1:length-1]
	print(url_str)
	
	# response = requests.get(url_str)
	# # response.raise_for_status
	# print(type(response))
	# byteImg = BytesIO(response.content)
	# print(type(byteImg))
	# # jpgImg = convert_cr2_to_jpg(byteImg)
	# image = Image.open(byteImg)
	
	# image = io.imread(url_str)

	# resp = urllib.request.urlopen(url_str)
	# image = np.asarray(bytearray(resp.read()), dtype="uint8")
	# image = cv.imdecode(image, cv2.IMREAD_COLOR)

	# headers = {"User-Agent": "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.3"}
	# req = Request(url=url_str, headers=headers) 
	# html = urlopen(req).read()
	# print(html)
	# print(url_str)
	# with urlopen(url_str) as url:
	# 	f = BytesIO(url.read())

	# img = Image.open(f)
	with urlopen(url_str) as url:
		with open('temp.jpg', 'wb') as f:
			f.write(url.read())

	# img = Image.open('temp.jpg')

	# img.show()
	return 'temp.jpg'

while True:
	print("[INFO] Awaiting user authentication...")
	ref = db.reference('/Benjamin/target')
	target = ref.get()
	ref = db.reference('/Benjamin/image')
	image = ref.get()
	type(image)
	# ref = db.reference('/test')
	# test = ref.set(1)
	# test = ref.get()
	# print(type(test))
	# test.delete()
	time.sleep(3)
	#
	# image = "images/baggage_claim.jpg"
	# target = "suitcase"
	if (target != None and image != None):
		print("[INFO] Photo taken!")
		image = download_image()
		img_read(image,target)
		ref = db.reference('/Benjamin/target')
		ref.delete()
		ref = db.reference('/Benjamin/image')
		ref.delete()
		time.sleep(10)
	ref = db.reference('/Benjamin/result')
	ref.delete()
