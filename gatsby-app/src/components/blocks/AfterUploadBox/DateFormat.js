import styled from 'styled-components'

const DateFormat = styled('div')`
  margin-left: 5px;

  > span {
    display: inline-block;
    width: 17px;
    
    &:last-child {
      width: 14px;
    }
    
    &.date {
      width: 40px;
      margin-right: 2px;
    }
  }
`

export default DateFormat
