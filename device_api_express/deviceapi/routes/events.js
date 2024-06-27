// IMPORTS
const express = require('express');
const router = express.Router();
const Event = require('../model/Event');
const isAuthorized = require('../middleware/isAuthorized');

// ROTAS

/* GET events listing. */
router.get("/", isAuthorized, async function(req, res) {
  return  res.json(await Event.find());
});

// Obter um evento pelo ID
router.get("/:id", isAuthorized, async (req, res) => {
  const {id} = req.params;

  const result = await Event.findById(id);
  return result 
    ? res.json(result)
    : res.status(404).send();
});

// Criar uma evento
router.post("/", isAuthorized, async (req, res) => {
  const json = req.body;

  const event = new Event(json);

  const hasErrors = event.validateSync();

  return hasErrors
    ? res.status(400).json(hasErrors)
    : res.status(201).json(await event.save());
});


router.put("/:id", isAuthorized, async (req,res)=>{
  const {id} = req.params;
  const json = req.body;

  json.updatedAt = new Date();

  const result = await Event.findByIdAndUpdate(id, json);
  
  return result 
    ? res.json(result)
    : res.status(404).send();
});

router.delete("/:id", isAuthorized, async (req,res)=>{
  const {id} = req.params;

  const result = await Event.findByIdAndDelete(id);

  return result 
    ? res.status(200)
    : res.status(404).send();
});

// EXPORT DO MÓDULO
module.exports = router;
