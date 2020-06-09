import styled, { css } from 'styled-components'

const focused = css`
  background-color: #0664D4;
  color: #FFFFFF;
`

const Text = styled('span')`
  ${props => props.focused && focused};
`

export default Text
