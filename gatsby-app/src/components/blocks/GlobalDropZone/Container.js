import styled from 'styled-components'

const Container = styled('div')`
  width: 100%;
  height: 100%;
  border: 5px dashed ${props => props.theme.colors.primary.two}; 
  display: flex;
  align-items: flex-end;
  justify-content: center;
  border-radius: ${props => props.theme.border.radius};
`

export default Container
