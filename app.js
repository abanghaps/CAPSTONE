const express = require('express')
const bodyParser = require('body-parser')
const cors = require('cors')
const app = express()

// Baru kita pake
app.use(express.urlencoded({ extended: true }))
app.use(cors());
app.use(bodyParser.json());

// Route ...
app.get('/', (req, res) => {
    res.send('It is US, MicroBizMate - new dev')
})
// 1. addStory
const storiesRouter = require('./src/routes/addStories') // Make sure the path is correct
app.use('/v1/stories', storiesRouter);
// 2. getAllStories
app.use('/v1/stories', require('./src/routes/getAllStories'));



// Jalanin Server
const port = 8080
app.listen(port, () => {
    console.log(`Server is running on port ${port}`);
})