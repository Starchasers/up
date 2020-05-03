import styled from 'styled-components'
import { p, mt, mb } from 'styled-components-spacing/dist/cjs'

const Pre = styled('pre')`
  ${mt(4)};
  ${mb(5)};
  ${p(2)};
  border-radius: 4px;
  background-color: ${props => props.theme.colors.secondary.two};
  opacity: 0.9;
`

export default Pre
