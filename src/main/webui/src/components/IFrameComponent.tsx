import { IFrameState } from "./IFrameState.tsx";
import {RefObject, useEffect, useRef} from "react";

const initIframe = (ref: RefObject<HTMLIFrameElement>, id: string, value: string) => {
    setTimeout(() => {
        ref.current?.contentWindow?.postMessage({
            action: "init",
            correlationId: id,
            state: { value: value }
        }, '*');
    }, 500);
}

// @ts-ignore
export const IFrameComponent = ({baseUrl, fieldName, fieldValue}) => {
  const fieldId = Math.floor(Math.random() * 10000) + "";
  const iframeRef = useRef<HTMLIFrameElement>(null);

    useEffect(() => {
        initIframe(iframeRef, fieldId, fieldValue);
        return () => {};
    }, [fieldValue]);

  return (
      <>
        <iframe ref={iframeRef} id={fieldId} className="w-100 h-100" src={baseUrl}></iframe>
        <IFrameState fieldId={fieldId} fieldValue={fieldValue} fieldName={fieldName}/>
      </>
  )
}
