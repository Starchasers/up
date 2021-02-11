import styled from 'styled-components'

const Content = styled('main')`
  position: relative;
  grid-area: content;
  min-height: 125px;
  z-index: 1;

  background: ${props => props.theme.palette.background};

  @media (max-width: ${props => props.theme.breakpoints.sm}) {
    margin-top: 125px;
  }
`

export default Content
