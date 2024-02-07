import {IFrameState} from "./IFrameState.tsx";

// @ts-ignore
export const IFrameComponent = ({baseUrl, fieldName, fieldValue}) => {
  const fieldId = fieldName + Date.now();

  return (
      <>
        <iframe id={fieldId} className="w-100 h-100"
                src={`${baseUrl}?id=${encodeURIComponent(fieldId)}&value=${encodeURIComponent(fieldValue)}`}></iframe>
        <IFrameState fieldId={fieldId} fieldValue={fieldValue} fieldName={fieldName}/>
      </>
  )
}
