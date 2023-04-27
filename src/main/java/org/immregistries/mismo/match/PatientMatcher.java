package org.immregistries.mismo.match;

import org.immregistries.mismo.match.model.Patient;

public class PatientMatcher {

  public static final String MATCHER_CONFIG_2015 =
      "Generation:0;World Name:MIIS-C;Island Name:Boston19;Score:0.8768426058012363;Born:2012.01.12 07:39:31 MST;Match:0.5976539440304252:0.0::{Household:0.5340047021220341:0.0::{Last Name:0.7394477915760619:0.16083169281725096::{L-match:0.8975774524536817:0.0::}{L-similar:0.5769135677880574:0.0::}{L-hyphenated:0.6:0.0::}}{Guardian:0.3:0.0::{GF-match:0.5:0.0::}{GL-match:0.46027300915789704:0.0::}{MM-match:0.17714551350571317:0.0::}}{Location:0.3:0.0::{PN-match:0.5:0.08589466214027962::}{SA-match:0.9673598834793466:0.4559152605261425::}{S-match:0.19119768322727826:0.0::}}}{Person:0.6:0.0::{Patient Id:0.0:0.0::{MRN-match:0.9:0.008616028169453616::}{SSN-match:0.7:0.4762401412904794:disabled:}{MA-match:0.3854594836970239:0.2912968817242621:disabled:}}{First Name:0.6539430754899886:0.022285108492698935::{F-match:0.9337334077507853:0.11714645904840293::}{F-similar:0.12639866792151896:0.0::}{F-middle:0.3461773101772462:0.007532771294044394::}{A-match:0.4:0.16664034418411822::}{G-match:0.1:0.0::}}{Birth Order:0.5488399547889631:0.0::{BO-matches:0.17056518914444302:0.012133046081512939::}{MBS-no:0.5:0.0::}}{DOB:0.4:0.16099709260030182::{DOB-match:1.0:0.36096395805526216::}{DOB-similar:0.1428016622436511:0.12050050021621805::}}{Middle Name:0.1097499668277081:0.0::{M-match:1.0:0.0::}{M-initial:0.11158025059378063:0.0::}{M-similar:0.05221669388457371:0.0::}{S-match:0.17339445105215423:0.0::}}{Shot History:0.2:0.0::{SH-match:0.8434711196989159:0.0::}}};Not Match:0.44421419906866344:0.12240703311770455::{Household:0.49680346080176185:0.20163533642328246::{Last Name:0.4:0.0::{L-not-match:1.0:0.0:not:}{L-not-similar:0.607962842245958:0.43248744317423493:not:}{L-not-hyphenated:0.6:0.0:not:}}{Guardian:0.3:0.12543614754336277::{GF-not-match:0.8772890703808285:0.0038207325234260736:not:}{GL-not-match:0.5:0.0:not:}{MM-not-match:0.6156030399580353:0.038992047203868174:not:}{MM-not-similar:0.530906191802011:0.2699050811578392:not:}}{Location:0.44207784318945:0.0::{PN-not-match:0.4:0.0:not:}{SA-not-match:0.6625441818813038:0.0920940731120864:not:}{S-not-match:0.3:0.2619251366713534:not:}}}{Person:0.1761757252522036:0.0::{Patient Id:0.4864546336460728:0.0::{MRN-not-match:0.05:0.0:not:}{SSN-not-match:0.9448590837670895:0.24338815020508475:not,disabled:}{MA-not-match:0.8879184240368448:0.0:not,disabled:}}{First Name:0.5803199159650172:0.010349465858667473::{F-not-match:0.3:0.2159008837549209:not:}{F-not-similar:0.3:0.05010846114037204:not:}{F-not-middle:0.2841023560998641:0.028499922506510345:not:}{A-not-match:0.0:0.0:not:}{G-not-match:0.9851122869818935:0.0:not:}}{Birth Order:0.01572755336230108:0.0::}{DOB:0.12408413338661316:0.008017702745740651::{DOB-not-match:0.7785390891446896:0.2683494770446756:not:}{DOB-not-similar:0.8043120733536863:0.035384086215674015:not:}}{Middle Name:0.9194439329366614:0.7527277958973195::{M-not-match:1.0:0.4750592342546145:not:}{M-not-initial:0.9979020506695768:0.0631956946954324:not:}{M-not-similar:0.6:0.04038705921822772:not:}{S-not-match:0.1:0.0:not:}}{Shot History:0.2:0.0::{SH-not-match:0.31664212470651:0.0:not:}}};Suspect Twin:1.0:0.0::{Name Different:0.2:0.0::{F-not-match:0.5:0.0:not:}{F-not-similar:0.6:0.0:not:}{M-not-match:0.5:0.0:not:}{G-not-match:0.5:0.0:not:}}{Birth Date:0.2:0.0::{DOB-match:1.0:0.0::}}{Birth Status:0.6:0.0::{MBS-maybe:0.5:0.0::}{MBS-yes:0.1:0.0::}};Missing:1.0:0.0::{Household:0.87705735766419:0.0::{L-missing:0.7064672667764026:0.0::}{Household:0.4:0.0::{GFN-missing:0.3:0.0::}{GLN-missing:0.19190311458033785:0.014397094425822488::}{MMN-missing:0.3:0.0::}}{Location:0.7208979096176452:0.0::{PN-missing:0.2:0.0::}{AS1-missing:0.4:0.0::}{AS2-missing:0.0:0.0::}{AC-missing:0.0:0.0::}{AS-missing:0.05:0.0::}{AZ-missing:0.0:0.0::}}}{Person:0.6:0.026859283296303826::{Patient Id:0.3:0.28262972979105583::{MRN-missing:0.9103716296607758:0.007178047110102764::}{SSN-missing:0.4:0.0:disabled:}{MA-missing:0.4:0.0:disabled:}}{First Name:0.3:0.0::{F-missing:1.0:0.0::}{A-missing:0.0543372636540701:0.0::}{S-missing:0.0:0.0::}{G-missing:0.4464521217969287:0.0::}}{Birth Order:0.1:0.0::{MBS-missing:1.0:0.0::}{BM-missing:0.5:0.0::}{BO-missing:0.6017422488947954:0.0::}}{DOB-missing:0.4:0.0::}{M-missing:0.2:0.0::}{SH-missing:0.2:0.0::}};";

  private String matcherConfig;

  public String getMatcherConfig() {
    return matcherConfig;
  }

  public PatientMatcher() {
    matcherConfig = MATCHER_CONFIG_2015;
  }

  public PatientMatcher(String matcherConfig) {
    this.matcherConfig = matcherConfig;
  }

  public PatientMatchResult match(Patient patientA, Patient patientB) {
    PatientMatchResult patientMatchResult = new PatientMatchResult();
    PatientCompare patientCompare = new PatientCompare();
    patientCompare.readScript(matcherConfig);
    patientCompare.setPatientA(patientA);
    patientCompare.setPatientB(patientB);
    String result = patientCompare.getResult();
    if (result.equals("Possible Match")) {
      patientMatchResult.setDetermination(PatientMatchDetermination.POSSIBLE_MATCH);
    } else if (result.equals("Match")) {
      patientMatchResult.setDetermination(PatientMatchDetermination.MATCH);
    } else {
      patientMatchResult.setDetermination(PatientMatchDetermination.NO_MATCH);
    }
    patientMatchResult.addMatchSignature(
        new MatchSignature(patientCompare.getSignature(), MatchSignatureType.PRIMARY));
    patientMatchResult.addMatchSignature(
        new MatchSignature(patientCompare.getSignature(1), MatchSignatureType.SECONDARY));
    patientMatchResult.addMatchSignature(
        new MatchSignature(patientCompare.getSignature(2), MatchSignatureType.TERTIARY));
    return patientMatchResult;
  }

  public String generateSignature(Patient patientA, Patient patientB) {
    PatientCompare patientCompare = new PatientCompare();
    patientCompare.readScript(matcherConfig);
    patientCompare.setPatientA(patientA);
    patientCompare.setPatientB(patientB);
    return patientCompare.getSignature();
  }
}
