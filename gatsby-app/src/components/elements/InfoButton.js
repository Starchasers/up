import React from 'react'
import Info from '../blocks/Info'
import { faInfoCircle } from '@fortawesome/free-solid-svg-icons'

const InfoButton = () => (
  <Info>
    <Info.Icon icon={faInfoCircle}/>
    <Info.TransitionDiv>
      <Info.Popup>
        <Info.Text>
          Did you know that you can paste <b>images</b> and <b>text</b> from your clipboard directly into our website?
        </Info.Text>
      </Info.Popup>
    </Info.TransitionDiv>
  </Info>
)

export default InfoButton
