import React from 'react'
import PropTypes from 'prop-types'
import { Helmet } from 'react-helmet'
import GlobalStyles from './globalStyles'
import metaTags from './elements/metaTags'

const Layout = ({ children }) => (
  <>
    <Helmet
      title="UP | Share your dreams"
      meta={metaTags}
    >
      <html lang='en-US'/>
    </Helmet>
    <GlobalStyles/>
    {children}
  </>
)

Layout.propTypes = {
  children: PropTypes.node.isRequired,
}

export default Layout
