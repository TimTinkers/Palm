// Imports and application setup.
var express = require('express');
var bodyParser = require('body-parser')
var app = express();
app.use(express.static('static'));
app.set('view engine', 'ejs');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({
	extended: true
}));

// Redirect visitors to the dashboard.
app.get("/", function (req, res) {
	res.render("dashboard");
});

// Launch the application and begin the server listening.
app.listen(3000, function () {
	console.log("Game exchange server listening on port 3000.");
});
