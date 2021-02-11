import styled from 'styled-components'

/**
 * Wrapper for `Button` element
 */

const ButtonContainer = styled('div')`
  display: grid;
  grid-template-columns: ${props => props.columns || 'auto 1fr'};
`

export default ButtonContainer
