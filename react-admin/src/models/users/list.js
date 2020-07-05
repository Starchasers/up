import React from 'react'
import {
  List,
  Datagrid,
  TextField,
  EditButton
} from 'react-admin'

export default (props) => (
  <List
    {...props}
  >
    <Datagrid>
      <TextField
        source='_id'
      />
      <EditButton />
    </Datagrid>
  </List>
)
