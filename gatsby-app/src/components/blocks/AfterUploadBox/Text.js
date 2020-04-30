import styled from 'styled-components'
import { px } from 'styled-components-spacing/dist/cjs'

const Text = styled('a')`
  color: ${props => props.theme.colors.text.one};
  text-decoration: none;
  line-height: 25px;
  width: 100%;
  white-space: nowrap;
  ${px(2)};
  text-align: center;
  
  &:hover {
    text-decoration: underline;
  }
`

export default Text
