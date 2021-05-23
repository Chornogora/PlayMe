export class MetronomeConfiguration {
  enabled: boolean;
  metronomeId: string;

  equals(externalConfiguration): boolean {
    return this.enabled === externalConfiguration.enabled
      && this.metronomeId === externalConfiguration.metronomeId;
  }
}
