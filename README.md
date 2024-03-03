# PhishNet

## Inspiration

Our journey with PhishNet started with a simple idea: to make the digital world a safer place for everyone. According to Statistics Canada over a third of Canadians had been targeted by phishing scams during the pandemic, with many suffering losses of time, data, or money.

We realized that scammers often prey on those who aren't as technologically literate, and we wanted to change that. That's why we created PhishNet a tool to give every user, especially those who aren't familiar with spotting scams, the protection they deserve.

## What it does

PhishNet is an Android SMS application designed to effectively filter out scam, spam, and phishing messages. Leveraging advanced lightweight deep learning models, Solace's PubSub+ event broker, and VirusTotal's API integration, PhishNet ensures a secure and seamless messaging experience for users. With PhishNet, users can trust that their inbox remains free from potential threats, allowing for enhanced peace of mind while communicating via SMS.

## How we built it

The main components of our project can be broken down into:
* The Android app (Java)
* Solace's PubSub+ event brokers & The server (Python)
* Message Analysis (Pytorch, HuggingFace & VirusTotal API)

## Android App:

Using some basic fragments, local storage, and some simple styling, we were able to emulate a messaging app that blocks scam/spam messages, before you can even get in a (phishing) net! By leveraging Solace's PubSub+ and MQTT Senders and Receivers, we were able to effectively connect our text message flagger to our Android Application. We were then able to immediately capture and filter out any scam/spam message sent to the user. We wanted to make an app that would replace the default messaging app, and be user friendly to all types of users!

## Solace's PubSub+ event brokers & Python server

The event brokers are what allowed for a link to be made between the Android app, and our backend. In our Solace cloud, we have two queues: Messages & Verdicts. The messages queue holds the messages that are to be evaluated by message analysis. The Android app sends any message it receives into this queue. The Python server has a persistent message consumer to retrieve the message from this queue. Then, the server runs any incoming message through our analysis algorithms. After being flagged as a scam or not, the messages with their newly calculated verdicts are put into the Verdicts queue by the server's persistent message publisher. The Android app then retrieves the message from Verdicts, and then either shows it to the user or not depending on the verdict.

## Message Analysis:

The predictive model harnesses two key technologies: a lightweight deep learning model for spam detection and VirusTotal's API for identifying phishing and potentially malicious links.
The spam detection component is built upon fine-tuning the base uncased version of DistilBERT. Through training on an open dataset comprising over 5500 text messages labeled as either spam or not spam, this model effectively distinguishes spam messages from regular conversation.
Complementing this, VirusTotal's API conducts thorough analyses of potential phishing links, cross-referencing them against an extensive database of known malicious websites. Additionally, it employs third-party checkers for independent assessments of link safety.
The API generates a comprehensive security report, which is analyzed to determine the overall threat level of the message sent to the user. This integrated approach ensures robust protection against both spam and phishing attempts, safeguarding users' messaging experiences with high accuracy.

## Challenges we ran into

One challenge was our lack of Android app development. We have the most experience with WebApps, so it took some effort to get used to the new challenge.

We got the Python receiver and publisher for Solace PubSub+ up relatively quickly, thanks to the comprehensive documentation. However, we ran into many issues trying to use similar logic on the Android side in Java. After a few hours, we then switched to MQTT after realizing it was the better option for a mobile app.

## Accomplishments that we're proud of


## What we learned

We learned a lot about developing for Android, as well as how event brokers work. We gained knowledge regarding using efficient LLM'S for binary sequence classification, ML deployment technique and integrating API's for meaningful predictions.

## What's next for PhishNet

We aim to include support for large text messages, as we have a size limit currently. Furthermore, we want to run our message analysis on a larger corpus of data to improve our generalization outside of the training and test set.

For the app, we aim to include more features such as attachments, contacts, group chats and quarantine chats. Quarantine chats would be the place most phishy texts meet their end. There we would implement the user choice whether or not to keep a text if they believe it wasn't spam/scam.

## Sources
### Dataset
**Scam Messages Dataset:** https://www.kaggle.com/datasets/uciml/sms-spam-collection-dataset/data

### Model Documentation:

**DistilBERT Paper:** https://arxiv.org/pdf/1910.01108.pdf

**BibTeX** @article{DBLP:journals/corr/abs-1910-01108,
  author       = {Victor Sanh and
                  Lysandre Debut and
                  Julien Chaumond and
                  Thomas Wolf},
  title        = {DistilBERT, a distilled version of {BERT:} smaller, faster, cheaper
                  and lighter},
  journal      = {CoRR},
  volume       = {abs/1910.01108},
  year         = {2019},
  url          = {http://arxiv.org/abs/1910.01108},
  eprinttype    = {arXiv},
  eprint       = {1910.01108},
  timestamp    = {Tue, 02 Jun 2020 12:48:59 +0200},
  biburl       = {https://dblp.org/rec/journals/corr/abs-1910-01108.bib},
  bibsource    = {dblp computer science bibliography, https://dblp.org}
}
#### HuggingFace Documentation:

**Training Documentation:** https://huggingface.co/docs/transformers/en/training

**Model Card:** https://huggingface.co/distilbert/distilbert-base-uncased





