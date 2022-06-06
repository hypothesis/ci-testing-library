#!groovy

/**
 * Push a pre-tagged Docker build to a registry, and update the `latest` tag.
 *
 * Named parameters:
 *
 *     `image` (required): the Docker image object to push
 *     `url` (optional): the Docker registry URL
 *     `credentialsId` (optional): the ID of the registry credentials to use
 *
 * Usage (e.g. in a pipeline script):
 *
 *     releaseApp(image: image)
 *
 * N.B. This function assumes that the passed image has already been
 *      appropriately tagged. Ensure this by passing images generated by the
 *      `buildApp` function.
 */
def call(Map parameters = [:]) {
    image = parameters.image
    // By default use a blank URL, i.e. the public Docker Hub.
    url = parameters.get('url', '')
    // By default use the default Docker Hub credentials.
    credentialsId = parameters.get('credentialsId', 'docker-hub-build')

    if (!image) {
        error 'you must provide a docker image'
    }

    docker.withRegistry(url, credentialsId) {
        image.push()
        image.push('latest')
    }
}