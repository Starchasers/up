import React from 'react'
import Info from '../blocks/Info'

const InfoButton = () => (
  <Info>
    <Info.Icon>i</Info.Icon>
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
