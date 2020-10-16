import styled from 'styled-components'
import { pl } from 'styled-components-spacing/dist/cjs'
import TextBox from './TextBox'
import Link from './Link'
import Back from './Back'
import Icon from './Icon'
import Button from './Button'
import Tooltip from './Tooltip'
import Area from './Area'
import Text from './Text'
import DeleteTime from './DeleteTime'
import DeleteTimeArea from './DeleteTimeArea'
import DateFormat from './DateFormat'

const AfterUploadBox = styled('div')`
  width: 100%;
  display: flex;
  justify-content: center;
  flex-direction: column;
  min-height: 300px;
  ${pl({md: 4})};
  position: relative;
`

AfterUploadBox.Back = Back
AfterUploadBox.Area = Area
AfterUploadBox.Button = Button
AfterUploadBox.Icon = Icon
AfterUploadBox.Icon = Icon
AfterUploadBox.Link = Link
AfterUploadBox.Text = Text
AfterUploadBox.TextBox = TextBox
AfterUploadBox.Tooltip = Tooltip
AfterUploadBox.DeleteTimeArea = DeleteTimeArea
AfterUploadBox.DeleteTime = DeleteTime
AfterUploadBox.DateFormat = DateFormat

export default AfterUploadBox
