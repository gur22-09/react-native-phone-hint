import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import { initPhoneNumberHint } from 'react-native-phone-hint';

export default function App() {
  const [result, setResult] = React.useState<string | undefined>();

  React.useEffect(() => {
    initPhoneNumberHint().then((no) => {
      setResult(no);
    });
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
