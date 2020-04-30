import styled from 'styled-components'
import { px, py } from 'styled-components-spacing/dist/cjs'

const TextBox = styled('div')`
  display: flex;
  width: 100%;
  margin: 0 auto;
  border-radius: ${props => props.theme.border.radius};
  ${px(3)};
  ${py(2)};
  background-color: ${props => props.theme.colors.secondary.two};
  justify-content: center;
  align-items: center;
  overflow-y: hidden;
  overflow-x: auto;
`

export default TextBox
