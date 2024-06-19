const express = require('express');
const multer = require('multer');
const instance = require('../config/firebase');
const { bucket, upload, db } = require('../config/firebase');
// Multer configuration for handling file uploads
const storage = multer.memoryStorage();

// @desc Add New Story
// @route POST /stories
// @access private ( requires token )

const userAddStories = async (req, res) => {
    try {
        // Pastikan Anda memiliki cara otentikasi yang sesuai di sini, seperti session atau cookie

        // Access other form fields from the request body
        const description = req.body.description;

        // Handle the uploaded photo
        const photo = req.file;
        if (!photo) {
            return response(400, null, "Photo is required", res);
        }

        // Here, you can process the photo, save it to storage, and get a URL
        // For simplicity, let's assume you save it to a variable called photoURL
        const filename = `stories/${Date.now()}_${photo.originalname}`;
        const file = bucket.file(filename);
        const stream = file.createWriteStream({
            metadata: {
                contentType: photo.mimetype,
            },
        });

        stream.on('error', (err) => {
            console.error(err);
            res.status(500, err, "Failed to upload photo", res);
        });

        stream.on('finish', async () => {
            // Get the public URL of the uploaded photo

            // Save the new story to the database
            await db.collection('stories').add({
                description: description,
                photoURL: filename,
                createdAt: new Date(),
            });

            res.status(200).json({ error: false, message: "success" });

        });

        // Pipe the photo data to the Cloud Storage file stream
        stream.end(photo.buffer);
    } catch (error) {
        console.error(error);
        res.status(400).json({ error: true, message: "Failed to add new story" });
        console.log(error);
    }
};

module.exports = userAddStories;
