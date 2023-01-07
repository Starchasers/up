import { css } from '@emotion/css'
import styled from '@emotion/styled'
import Countdown, { CountdownRendererFn } from 'react-countdown'

const DateFormat = styled('div')`
  font-size: 12px;

  > span {
    display: inline;
    width: 17px;
    color: ${(props) => props.theme.colors.timberwolf};

    &:last-child {
      width: 14px;
    }

    &.date {
      width: 40px;
      margin-right: 2px;
    }
  }
`

interface ITimerCountdown {
  date: Date
}

const dateRenderer: CountdownRendererFn = ({ days, hours, minutes, seconds }) => (
  <DateFormat>
    <span>Expires in: </span>
    {days !== 0 ? <span className='date'>{days + (days === 1 ? ' day,' : ' days,')}</span> : null}
    <span>{hours < 10 ? `0${hours}:` : `${hours}:`}</span>
    <span>{minutes < 10 ? `0${minutes}:` : `${minutes}:`}</span>
    <span>{seconds < 10 ? `0${seconds}` : `${seconds}`}</span>
  </DateFormat>
)

const TimerCountdown = (props: ITimerCountdown) => {
  return (
    <div
      className={css`
        position: relative;
        display: flex;
        justify-content: center;
        align-items: center;
        user-select: none;
        flex-direction: row;
        //box-shadow: 0 4px 20px 0 rgb(31 34 41 / 30%);
        //border: 2px solid #1f2229;
        //background: #1f2229;
        padding: 5px 10px;
        border-radius: 8px;
        transition: all 250ms;
        opacity: 0.7;
      `}
    >
      <Countdown date={props.date} zeroPadTime={2} renderer={dateRenderer} />
    </div>
  )
}

export default TimerCountdown
