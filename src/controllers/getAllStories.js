const express = require('express');
const { db, bucket } = require('../config/firebase');

const router = express.Router();

// @desc Get All Stories
// @route GET /stories
// @access public

const getAllStories = async (req, res) => {
    try {
        // Access query parameters
        const page = parseInt(req.query.page) || 1;
        const size = parseInt(req.query.size) || 10;
        const location = parseInt(req.query.location) || 0;

        // Fetch stories based on location
        let storiesRef = db.collection('stories/');

        const snapshot = await storiesRef.orderBy('createdAt', 'desc').limit(size).offset((page - 1) * size).get();

        const listStory = [];

        for (const doc of snapshot.docs) {
            const data = doc.data();

            // Handle the case where the user's document is not found
            listStory.push({
                description: data.description,
                photoUrl: data.photoURL // Include the photoURL field
            });

        }

        res.status(200).json({ error: false, message: "Stories fetched successfully", listStory });
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: true, message: "Failed to fetch stories" });
    }
};

module.exports = getAllStories;
