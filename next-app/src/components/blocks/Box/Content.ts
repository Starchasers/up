import styled from '@emotion/styled'

const Content = styled('div')`
  position: relative;
  display: grid;
  grid-template-columns: 1fr 2fr;
  grid-column-gap: 25px;
  grid-row-gap: 25px;
  background: ${(props) => props.theme.colors.mako};
  border-radius: 8px;
  padding: 25px;
  z-index: 1;

  @media (max-width: ${(props) => props.theme.breakpoints.md}) {
    grid-template-columns: 1fr;
  }
`

export default Content
