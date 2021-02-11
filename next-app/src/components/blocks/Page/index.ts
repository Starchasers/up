import styled from 'styled-components'

import Content from './Content'
import Footer from './Footer'

const Page = styled('div')`
  display: grid;
  min-height: 100vh;
  width: 100%;

  background: ${props => props.theme.palette.background};

  grid-template-columns: auto;
  grid-template-areas:
    'content'
    'footer';
  grid-template-rows: 1fr auto;

  @media (max-width: ${props => props.theme.breakpoints.md}) {
    grid-template-columns: 100%;
  }

  @media (max-width: ${props => props.theme.breakpoints.sm}) {
    grid-template-rows: 1fr auto;
  }
`

Page.Content = Content
Page.Footer = Footer

export default Page
