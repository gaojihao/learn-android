import React, { Component } from 'react';
import { View,FlatList,StyleSheet,Text,TouchableOpacity } from 'react-native';
import EmptyView from './../../component/EmptyView';
import config from './../../tools/config';

export default class QuestionScreen extends Component {
  static navigationOptions = {
    title: '我的随访',
  };

  constructor(props) {
    super(props);
    this.state = JSON.parse('{\"list\":[{\"status\":\"0\",\"surveyId\":\"fa7022f13d824d1d9334a94cf4d86d1f\",\"surveyName\":null,\"answerAt\":null,\"surveyUrl\":\"/survey/fa7022f13d824d1d9334a94cf4d86d1f\",\"statusName\":\"进行中\",\"surveyDate\":\"2019-09-02\"},{\"status\":\"0\",\"surveyId\":\"fa7022f13d824d1d9334a94cf4d86d1f\",\"surveyName\":null,\"answerAt\":null,\"surveyUrl\":\"/survey/fa7022f13d824d1d9334a94cf4d86d1f\",\"statusName\":\"进行中\",\"surveyDate\":\"2019-09-02\"}]}');
  }

  render() {

    return (
      <React.Fragment>
        {
          this.state.list.length >= 1?
          <FlatList
          style={{
              backgroundColor: 'white',
          }}
          data={this.state.list}
          showsVerticalScrollIndicator={false}
          showsHorizontalScrollIndicator={false}
          keyExtractor={(item, index) => `${item.id}${index}`}
          renderItem={this._renderItem}
      />
          :
          <EmptyView emptyTitle={'暂无内容'}/>
        }
      </React.Fragment>
    );
  }

  _renderItem = ({item,index}) =>(
    <TouchableOpacity activeOpacity={0.8} style={styles.itemStyle} key = {`${item.id}${index}`}>
      <View style={styles.contentStyle}>
        <Text style={styles.nornalText}>{item.surveyName}</Text>
        <Text style={styles.nornalText}>{item.statusName}</Text>
      </View>
    </TouchableOpacity>
    )
}

const styles = StyleSheet.create({
  itemStyle:{
      width:config.screenWidth,
      height:80,
      backgroundColor:'white',
      justifyContent:'center',
      alignItems:'center',
  },

  contentStyle:{
    width:config.screenWidth -30,
    height:70,
    backgroundColor:'#FE5E86',
    flexDirection:'row',
    justifyContent:'space-between',
    paddingHorizontal:15,
    alignItems:'center',
  },

  nornalText:{
    fontSize:14,
    color:'white',
  },

});

