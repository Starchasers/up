import React from 'react'
import PropTypes from 'prop-types'
import { Helmet } from 'react-helmet'
import metaTags from './elements/metaTags'

const Layout = ({ children }) => (
  <>
    <Helmet
      title="UP | Share your dreams"
      meta={metaTags}
    >
      <html lang='en-US'/>
    </Helmet>
    {children}
  </>
)

Layout.propTypes = {
  children: PropTypes.node.isRequired,
}

export default Layout
