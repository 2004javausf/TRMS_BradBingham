export interface Rform {
  ID: number;
  empID: number;
  status: string;
  supApr: string;
  supSubDate: string;
  headApr: string;
  headSubDate: string;
  coorApr: string;
  coorSubDate: string;
  isAltered: string;
  rejectMessage: string;
  formSubDate: string;
  startDate: string;
  startTime: string;
  location: string;
  cost: number;
  description: string;
  justification: string;
  gradeFormatID: number;
  eventType: string;
  onSubmit: Blob;
  finalGrade: number;
  gradeApr: string;
  finalPres: Blob;
  presApr: string;
}
