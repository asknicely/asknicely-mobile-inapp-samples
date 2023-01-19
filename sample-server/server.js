const http = require('http');
const crypto = require('crypto');
const fs = require('fs');

async function startup() {

  let data = null;

  try {
    if (fs.existsSync('config.json')) {
      const rawdata = fs.readFileSync('config.json');
      data = JSON.parse(rawdata);
    }
  } catch(err) {
    console.log(err)
  } finally {
    if (!data) {
      console.error("Could not find valid config.json; please create one from the config-template.json file")
      process.exit()
    }
  }

  console.log('Starting server with settings: ', data);

  function signHmacSha256(key, str) {
    //Here we generate a signed hmac hash which will be verified by AskNicely to create the survey
    let hmac = crypto.createHmac('sha256', Buffer.from(key, 'utf8'));
    let signed = hmac.update(str).digest('hex');
    return signed;
  }

  console.log('Email hash: ', signHmacSha256(data.server_params.email_hash, data.request_params.email));

  const requestListener = function (req, res) {
      res.setHeader('Content-Type', 'application/json');
      res.writeHead(200);

      const email_hash = signHmacSha256(data.server_params.email_hash, data.request_params.email);

      const result = {
        email_hash,
        ...data.request_params
      };
      res.end(JSON.stringify(result));
      console.log("Sending: ", result);
  };

  http.createServer(requestListener).listen(data.server_params.port);
}

startup();