import styled, { css } from 'styled-components'

const focused = css`
  background-color: #0664D4;
  color: #FFFFFF;
`

const Text = styled('span')`
  padding: 4px 0;
  ${props => props.focused && focused};
`

export default Text
