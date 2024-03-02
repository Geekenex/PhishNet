import os

import torch

from transformers import DistilBertForSequenceClassification, DistilBertTokenizerFast
path = os.path.abspath(os.path.join(os.getcwd(), os.pardir)) + "\Model"

tokenizer = DistilBertTokenizerFast.from_pretrained('distilbert-base-uncased')

model = DistilBertForSequenceClassification.from_pretrained(path + "\\trained1")

def scamcheck(text):
    tokenized_message = tokenizer(text, truncation=True, padding=True, return_tensors="pt")
    with torch.no_grad():
        outputs = model(**tokenized_message)
        predicted_label = torch.argmax(outputs.logits, dim=1).item()

    print(predicted_label)