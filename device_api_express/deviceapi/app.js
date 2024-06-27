const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const mongoose = require('mongoose'); // import

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var eventsRouter = require('./routes/events');
const isAuthorized = require('./middleware/isAuthorized');

require("dotenv").config();

var app = express();

// Configurar a conexão
mongoose
  .connect(`${process.env.MONGODB_URI}`, {})
  .then(() => {
    console.log("MongoDB conectado!");
  })
  .catch( (err) => {
    console.log("Falha ao conectar.");
    console.log(err);
  });

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

// Definição dos rotas da aplicação
app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/events', isAuthorized, eventsRouter);
app.use('/auth', require('./routes/auth'));

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;
