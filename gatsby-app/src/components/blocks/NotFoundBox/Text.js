import styled from 'styled-components'
import { mb, mt } from 'styled-components-spacing/dist/cjs'

const Text = styled('p')`
  color: ${props => props.theme.colors.text.one};
  ${mt(1)};
  ${mb(0)};
  text-align: center;
`

export default Text
