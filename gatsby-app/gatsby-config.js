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
        name: 'UP | File hosting',
        short_name: 'UP Hosting',
        start_url: '/',
        background_color: '#1A1B1C',
        theme_color: '#434755',
        icon: 'src/assets/images/favicon.png',
        display: 'standalone',
      },
    },
    {
      resolve: 'gatsby-plugin-favicon',
      options: {
        logo: './src/assets/images/favicon.png',
        appDescription: 'Private file hosting',
        developerName: 'Starchasers',
        lang: 'en-US',
        theme_color: '#434755',
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
    {
      resolve: 'gatsby-plugin-copy',
      options: {
        verbose: true,
        'static/robots.txt': 'robots.txt',
        'static/favicon.ico': 'favicon.ico',
      },
    },
    {
      resolve: `gatsby-plugin-layout`,
      options: {
        component: require.resolve(`./src/providers`),
      },
    },
    'gatsby-plugin-remove-serviceworker',
  ],
}

require('dotenv').config({
  path: `.env.${process.env.NODE_ENV}`,
})
