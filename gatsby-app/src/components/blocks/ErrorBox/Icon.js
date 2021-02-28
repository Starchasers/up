import styled from 'styled-components'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

const Icon = styled(FontAwesomeIcon)`
  color: ${props => props.theme.colors.text.one};
  font-size: 20px;
  margin-right: 10px;
`

export default Icon
