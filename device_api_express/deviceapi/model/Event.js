const mongoose = require("mongoose");

const EventSchema = new mongoose.Schema({
    userId: {type: String, required: true},
    deviceId: {type: String, required: true},
    eventType: {type: String, required: true},
    description: {type: String, required: true},
    deviceStatus: {type: String, required: true},
    createdAt: {type: Date, default: Date.now },
    updatedAt: {type: Date, default: Date.now }
});

module.exports = mongoose.model('Event', EventSchema, 'event');