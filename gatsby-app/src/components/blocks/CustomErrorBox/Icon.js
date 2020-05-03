import styled from 'styled-components'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

const Icon = styled(FontAwesomeIcon)`
  color: ${props => props.theme.colors.text.one};
  font-size: 40px;
  margin: 0 auto;
`

export default Icon
