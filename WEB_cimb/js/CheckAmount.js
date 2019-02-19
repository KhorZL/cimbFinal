var amount = document.getElementById('amount');
var payButton = document.getElementById('payButton');

payButton.onclick = function() {
	/*if (amount.value >= 1000) {
		var transporter = nodemailer.createTransport({
		  service: 'gmail',
		  auth: {
		    user: 'edathr2@gmail.com',
		    pass: 'S9829402C'
		  }
		});

		var mailOptions = {
		  from: 'edathr2@gmail.com',
		  to: 'eda_tan@mymail.sutd.edu.sg',
		  subject: 'Transaction Underway',
		  text: 'Hi, \n there is a transaction of more than 1000 dollars underway.' 
		  + ' If this transaction is not initiated by you, please contact CIMB Bank immediately.'
		  + '\n Regards, \n CIMB Bank'
		};

		transporter.sendMail(mailOptions, function(error, info){
		  if (error) {
		    console.log(error);
		  } else {
		    console.log('Email sent: ' + info.response);
		  }
		});

		// Find your account sid and auth token in your Twilio account Console.
		var client = new twilio('AC8e1088d0099d30e0d960250a07d6a1ee', '3517af04debb9caea334c5e9cc62a4a1');

		// Send the text message.
		client.messages.create({
		  to: '+65 9152 2607',
		  from: '+15615373900',
		  body: 'There is a transaction of more than 1000 numbers underway. If you are not initiating the transaction, '
		  + 'please contact CIMB Bank immediately.'
		});
	}*/
}




