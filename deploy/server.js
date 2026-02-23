const http = require('http');
const { exec } = require('child_process');
const url = require('url');

const PORT = 9000;
const TOKEN = process.env.DEPLOY_TOKEN || 'your-secret-token';

const server = http.createServer((req, res) => {
    const parsedUrl = url.parse(req.url, true);

    // Health check
    if (parsedUrl.pathname === '/health') {
        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({ status: 'OK', service: 'fitness-deploy' }));
        return;
    }

    // Deploy endpoint
    if (parsedUrl.pathname === '/deploy' && req.method === 'POST') {
        const token = parsedUrl.query.token;

        if (token !== TOKEN) {
            res.writeHead(401, { 'Content-Type': 'application/json' });
            res.end(JSON.stringify({ error: 'Unauthorized' }));
            return;
        }

        console.log(`[${new Date().toISOString()}] Deploying fitness production`);

        res.writeHead(200, { 'Content-Type': 'application/json' });
        res.end(JSON.stringify({
            status: 'deploying',
            env: 'prod',
            timestamp: new Date().toISOString()
        }));

        // Execute deployment in background
        const deployScript = `/app/deploy.sh`;
        exec(deployScript, (error, stdout, stderr) => {
            if (error) {
                console.error(`[${new Date().toISOString()}] Deploy error:`, error);
                console.error(stderr);
            } else {
                console.log(`[${new Date().toISOString()}] Deploy success:`, stdout);
            }
        });

        return;
    }

    res.writeHead(404, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ error: 'Not Found' }));
});

server.listen(PORT, () => {
    console.log(`Fitness Deploy Server running on port ${PORT}`);
});
