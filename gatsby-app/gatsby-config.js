module.exports = {
  siteMetadata: {
    title: 'UP Website',
    description: 'Up website for storing files',
    author: '@Kuboczoch',
  },
  plugins: [
    'gatsby-plugin-react-helmet',
    'gatsby-transformer-sharp',
    'gatsby-plugin-sharp',
    {
      resolve: 'gatsby-source-filesystem',
      options: {
        name: 'images',
        path: `${__dirname}/src/assets/images`,
      },
    },
    {
      resolve: `gatsby-plugin-react-redux`,
      options: {
        pathToCreateStoreModule: './src/redux/createStore',
        cleanupOnClient: true,
        windowKey: '__PRELOADED_STATE__',
      },
    },
    {
      resolve: `gatsby-plugin-typography`,
      options: {
        pathToConfigModule: `src/assets/typography/typography.js`,
      },
    },
    {
      resolve: 'gatsby-plugin-manifest',
      options: {
        name: 'up-website',
        short_name: 'up',
        start_url: '/',
        background_color: '#1A1B1C',
        theme_color: '#2c369a',
        icon: 'src/assets/images/favicon.png',
      },
    },
    {
      resolve: 'gatsby-plugin-favicon',
      options: {
        logo: './src/assets/images/favicon.png',
        appDescription: 'Private file hosting',
        developerName: 'Kuboczoch',
        lang: 'en-US',
        background: '#fff',
        theme_color: '#fff',
        display: 'standalone',
        orientation: 'any',
        version: '1.0',

        icons: {
          android: true,
          appleIcon: true,
          appleStartup: true,
          coast: false,
          favicons: true,
          firefox: true,
          yandex: false,
          windows: false,
        },
      },
    },
    {
      resolve: 'gatsby-plugin-styled-components',
      options: {
        displayName: false,
      },
    },
    'gatsby-plugin-offline',
  ],
}

require('dotenv').config({
  path: `.env.${process.env.NODE_ENV}`,
})
