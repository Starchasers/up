import React from 'react'
import PropTypes from 'prop-types'
import { Helmet } from 'react-helmet'
import { ThemeProvider } from 'styled-components'
import GlobalStyles from './globalStyles'
import { theme } from './theme'

const Layout = ({ children }) => (
  <>
    <Helmet
      title="UP | Share your dreams"
      meta={[
        {
          name: 'description',
          content: 'UP Website',
        },
        {
          name: 'charSet',
          content: 'utf-8',
        },
      ]}
    />
    <ThemeProvider theme={theme}>
      <GlobalStyles />
      {children}
    </ThemeProvider>
  </>
)

Layout.propTypes = {
  children: PropTypes.node.isRequired,
}

export default Layout
