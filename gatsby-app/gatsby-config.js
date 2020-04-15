module.exports = {
  siteMetadata: {
    title: `UP Website`,
    description: `Up website for storing files`,
    author: `@Kuboczoch`,
  },
  plugins: [
    `gatsby-plugin-react-helmet`,
    `gatsby-transformer-sharp`,
    `gatsby-plugin-sharp`,
    {
      resolve: `gatsby-plugin-manifest`,
      options: {
        name: `up-website`,
        short_name: `up`,
        start_url: `/`,
        background_color: `#663399`,
        theme_color: `#663399`,
      },
    }
  ],
}
