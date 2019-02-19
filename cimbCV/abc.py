# Initialize Firebase
# !pip install -q firebase-admin
import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

json = PATH/'helloworld-8cf45-firebase-adminsdk-a9v95-e28ad0985a.json'
cred = credentials.Certificate(str(json))
database_url = "https://helloworld-8cf45.firebaseio.com"
firebase_admin.initialize_app(cred, {'databaseURL': database_url})

'''
new version to work with nash database
changes:
1. User reference in the database is user id instead of user name
2. Users (dicts) are not contained in a folder rather than in the database root
3. Only download files if image url has changed, save time
4. No more get_user_dict, function was too simple and uneccessary
'''
import os
import requests
import matplotlib.pyplot as plt

def download(url, fname):
    padding = '"'
    url = remove_padding(url, padding)
    open(fname, 'wb').write(requests.get(url).content)
    print('Downloaded:', fname)

def create_user(database, userpath='', approved=0, user_type='Student',
                ref_image_url='', test_image_url='', name=str(None)):
    database.reference(userpath).set({
        'approved': approved,
        'ref_image_url': ref_image_url,
        'test_image_url': test_image_url,
        'name': name,
        'type': user_type
    })
    return database.reference(userpath).get()

def download_and_show_user_images(user_dict, url_changes):
    fnames = []
    for image_url_type in url_changes.keys():
        url = user_dict[image_url_type]
        fname = '{}_{}.jpg'.format(user_dict['name'], image_url_type)
        fnames.append(fname)
        if url_changes[image_url_type]:
            download(url, fname)
        plt.imshow(plt.imread(fname))
        plt.show()
    return fnames

import scipy
def compute_similarity(face_vectors):
    return scipy.spatial.distance.pdist(face_vectors)[0]

def update_approved_status(user_dict, status):
    user_dict['approved'] = status
    print('Updated firebase approved status to:', status)

def check_user_face(user_dict, url_changes,
                    threshold=1.0, model_dir=MODEL_DIR):
    fnames = download_and_show_user_images(user_dict, url_changes)
    try:
        cropped_images, face_vectors = compare_faces(model_dir, fnames) # call model
        similarity = compute_similarity(face_vectors)
        print('Vector distance (lower = more similar face):', similarity)
        facial_match_status = int(similarity < threshold)
    except:
        print('Compare faces failed, most likely no face found')
        facial_match_status = 0
    user_dict['approved'] = facial_match_status
    return user_dict

# Create demo user database in firebase with team members as users
# ID for profs are for demo, prefix is 111 instead of 100
USERS_FOLDER = 'users'  # Our internal folder in firebase

team = {
    'Nashita Abd': (1003045, 'https://i.imgur.com/8mNSJLL.jpg'),
    'See Yi Jie': (1002696, 'https://i.imgur.com/FYx78qm.jpg'),
    'Caleb Lee Liang Heng': (1002673, 'https://i.imgur.com/bmOOqRp.jpg'),
    'Chia Yew Ken': (1002675, 'https://i.imgur.com/1zjdaJ7.jpg'),
    'Han Jing Bertha': (1003120, 'https://i.imgur.com/E7tkQYk.jpg'),
    'Lim Ping An Benjamin': (1002658, 'https://i.imgur.com/qQLqGYf.jpg'),
    'Ngai Man Cheung': (1110000, 'https://i.imgur.com/86Xgl8Y.jpg'),
    'Norman Lee Tiong Seng': (1110001, 'https://i.imgur.com/owpxGxt.jpg')
}
for name, details in team.items():
    user, url = details
    create_user(
        database=db,
        userpath=os.path.join(USERS_FOLDER, str(user)),
        ref_image_url=url,
        name=name,
        user_type='Student' if str(user).startswith('100') else 'Staff'
    )
