import axios from 'axios'

const backendURL = process.env.GATSBY_API_URL ? process.env.GATSBY_API_URL : ''

const APIClient = {
  getConfiguration() {
    return axios.get(`${backendURL}/api/configuration`)
  },
  postFile(file, config) {
    return axios.post(`${backendURL}/api/upload`, file, config)
  }
}

export default APIClient
