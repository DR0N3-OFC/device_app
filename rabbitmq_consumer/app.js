const amqp = require('amqplib');
const mongoose = require('mongoose');
const express = require('express');
const cors = require('cors');

// Configuração do Mongoose para o MongoDB
mongoose.connect(`${process.env.MONGODB_URI}`)
  .then(() => console.log('Conectado ao MongoDB'))
  .catch(err => console.error('Erro ao conectar ao MongoDB:', err));

// Definir um schema e modelo para armazenar as mensagens
const messageSchema = new mongoose.Schema({ content: String });
const Message = mongoose.model('Message', messageSchema);

async function consumeMessages() {
  const connection = await amqp.connect('amqp://rabbitmq');
  const channel = await connection.createChannel();
  const queue = process.env.QUEUE;

  await channel.assertQueue(queue, { durable: true });
  console.log(`[*] Waiting for messages in ${queue}. To exit press CTRL+C`);

  channel.consume(queue, async (message) => {
    const msgContent = message.content.toString();
    console.log(`[x] Received ${msgContent}`);
    
    // Armazenar a mensagem no banco de dados
    const newMessage = new Message({ content: msgContent });
    await newMessage.save();

    // Confirmar a mensagem como processada
    channel.ack(message);
  }, { noAck: false });
}

consumeMessages().catch(console.error);

const app = express();
const port = process.env.PORT;

app.use(cors());

app.get('/messages', async (req, res) => {
  // Buscar todas as mensagens no banco de dados
  const messages = await Message.find({});
  res.json(messages);
});

app.listen(port, () => {
  console.log(`Servidor backend rodando em http://localhost:${port}`);
});
