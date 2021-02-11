import React from 'react'

import { IHomePageProps } from '../../pages/index'
import { IHomePageStateProps } from './useHomePage'

import HomePage from '../../components/blocks/HomePage'
import MainBox from '../../components/blocks/MainBox'

interface IHomePageViewProps extends IHomePageProps, IHomePageStateProps {

}

const HomePageView = (props: IHomePageViewProps) => (
  <HomePage>
    <HomePage.Container>
      <MainBox>
        <MainBox.Content>
          <MainBox.Grid>
            <div>
              a
            </div>
            <div>
              b
            </div>
          </MainBox.Grid>
        </MainBox.Content>
        <MainBox.ShadowBox>

        </MainBox.ShadowBox>
      </MainBox>
    </HomePage.Container>
  </HomePage>
)

export default HomePageView
