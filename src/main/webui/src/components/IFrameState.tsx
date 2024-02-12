import { useState } from "react";

const listeners: any = {};
const registerComponentChange = (id: string, currentValueFunction: () => string, listenerFunction: (v: string) => void) => {
    const eventFunction = (event: MessageEvent<any>) => {
        if (event.data.action === 'changeValue' && event.data.correlationId === id) {
            //console.log("Received event...");
            //console.log(event.data);
            const newValue = event.data.value;
            if (currentValueFunction() !== newValue) {
                listenerFunction(newValue);
            }
        }
    }
    if (listeners[id]) {
        window.removeEventListener("message", listeners[id]);
    }
    window.addEventListener("message", eventFunction);
    listeners[id] = eventFunction;
}

// @ts-ignore
export const IFrameState = ({fieldId, fieldName, fieldValue}) => {

    const [componentValue, setComponentValue] = useState(fieldValue)

    registerComponentChange(fieldId, () => componentValue , (newValue: string) => {
        setComponentValue(newValue);
        //console.log("CHANGE VALUE TO: " + newValue);
    });

    return (
      <>
        <input name={fieldName} type={'hidden'} value={componentValue}/>
        <p>Value: {componentValue}</p>
      </>
    )
}
