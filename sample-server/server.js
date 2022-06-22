const http = require('http');
// Node v17+ could use readline promises instead
const readline = require('readline');
const crypto = require('crypto');

async function startup() {
  const readlineInterface = readline.createInterface({
    input: process.stdin,
    output: process.stdout
  });

  function ask(questionText) {
    return new Promise((resolve, reject) => {
      readlineInterface.question(questionText, (input) => resolve(input) );
    });
  }

  let port = await ask('Please provide the port you wish to use (default 8080): ');
  if (!port) {
    port = 8080;
  }
  const domain_key = await ask('AskNicely tenant name (required): ');
  if (!domain_key) {
    console.log('We require a tenant to send the request to')
    process.exit()
  }
  const template_name = await ask('Target survey name (required): ');
  if (!domain_key) {
    console.log('We require the name of which survey you want to send')
    process.exit()
  }
  let name = await ask('User name to send to (optional): ');
  if (!name) {
    console.log('Using email instead of name')
    name = ''
  }
  const email = await ask('User email/identifier to send to (required): ');
  if (!email) {
    console.log('We require the email or an identifier of the person we are sending the survey to')
    process.exit()
  }
  const email_hash = await ask('Email hash to encode the email with (required): ');
  if (!email) {
    console.log('We require the email hash in order to secure the request')
    process.exit()
  }
  let joined = await ask('When the user joined your organisation (optional): ');
  if (!joined) {
    joined = ''
  }

  const data = {
    domain_key,
    template_name,
    name,
    email,
    joined
  }
  const settings = {
    port,
    email_hash,
    ...data
  }
  console.log('Starting server with settings: ', settings);

  readlineInterface.close();

  function signHmacSha256(key, str) {
    //Here we generate a signed hmac hash which will be verified by AskNicely to create the survey
    let hmac = crypto.createHmac('sha256', Buffer.from(key, 'hex'));
    let signed = hmac.update(str).digest('hex');
    return signed;
  }

  const requestListener = function (req, res) {
      res.setHeader('Content-Type', 'application/json');
      res.writeHead(200);

      const email_hashed = signHmacSha256(email_hash, email);

      const result = {
        email_hashed,
        ...data
      };
      res.end(JSON.stringify(result));
      console.log("Sending: ", result)
  };

  http.createServer(requestListener).listen(port);
}

startup();