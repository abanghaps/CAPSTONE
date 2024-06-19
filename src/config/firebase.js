const admin = require('firebase-admin')
const credentials = require('./key.json')
const multer = require('multer')

const firebaseAdmin = admin.initializeApp({
    credential: admin.credential.cert(credentials),
    storageBucket: 'gs://capstone-project13.appspot.com'
})

// initialize bucket
const bucket = admin.storage().bucket()
const upload = multer({
    storage: multer.memoryStorage(),
    limits: {
        fileSize: 5 * 1024 * 1024
    }
})

// initialize firestore database
const db = admin.firestore()

module.exports = {
    bucket,
    upload,
    db,
    firebaseAdmin,
}