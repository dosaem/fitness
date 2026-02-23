#!/bin/bash

# Add Docker to PATH (Synology ContainerManager)
export PATH="/var/packages/ContainerManager/target/usr/bin:$PATH"

REPO_DIR="/volume1/docker/fitness"
LOG_FILE="/volume1/docker/fitness/deploy.log"

log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | tee -a "$LOG_FILE"
}

log "========================================="
log "Starting Fitness deployment"
log "========================================="

cd "$REPO_DIR" || exit 1

# Git pull
log "Pulling latest changes..."
git fetch origin --prune
git checkout main
git reset --hard origin/main
log "Git updated to: $(git rev-parse --short HEAD)"

# Build and deploy
cd backend || exit 1

COMPOSE_FILE="docker-compose.prod.yml"
log "Deploying to PRODUCTION..."

log "Stopping existing containers..."
docker compose -f "$COMPOSE_FILE" down

log "Building new images..."
docker compose -f "$COMPOSE_FILE" build --no-cache

log "Starting containers..."
docker compose -f "$COMPOSE_FILE" up -d

log "Waiting for containers to be healthy..."
sleep 60

# Health check with retries
HEALTH_URL="http://localhost:8087/api/health"

for i in {1..5}; do
    HEALTH_RESPONSE=$(curl -s "$HEALTH_URL")
    if echo "$HEALTH_RESPONSE" | grep -q "ok"; then
        log "Health check passed on attempt $i!"
        log "Response: $HEALTH_RESPONSE"
        log "Deployment completed successfully!"
        exit 0
    else
        log "Health check attempt $i failed. Response: $HEALTH_RESPONSE"
        if [ $i -lt 5 ]; then
            log "Retrying in 15 seconds..."
            sleep 15
        fi
    fi
done

log "Health check failed after 5 attempts!"
log "========================================="
exit 1
