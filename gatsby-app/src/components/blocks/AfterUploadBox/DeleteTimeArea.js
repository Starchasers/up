import styled from 'styled-components'

const DeleteTimeArea = styled('div')`
  color: ${props => props.theme.colors.text.one};
  display: flex;
  position: absolute;
  bottom: 9px;
  right: 0;
  background-color: ${props => props.theme.colors.secondary.two};
  border-radius: 8px;
  font-size: 12px;
`

export default DeleteTimeArea
