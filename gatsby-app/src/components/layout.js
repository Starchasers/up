import React from 'react'
import PropTypes from 'prop-types'
import { Helmet } from 'react-helmet'
import { ThemeProvider } from 'styled-components'
import GlobalStyles from './globalStyles'
import { theme } from './theme'
import metaTags from './elements/metaTags'

const Layout = ({ children }) => (
  <>
    <Helmet
      title="UP | Share your dreams"
      meta={metaTags}
    >
      <html lang='en-US'/>
    </Helmet>
    <ThemeProvider theme={theme}>
      <GlobalStyles/>
      {children}
    </ThemeProvider>
  </>
)

Layout.propTypes = {
  children: PropTypes.node.isRequired,
}

export default Layout
