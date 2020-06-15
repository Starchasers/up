import styled from 'styled-components'

const Text = styled('span')`
  color: ${props => props.theme.colors.text.one};
  text-decoration: none;
  line-height: 1.5;
`

export default Text
