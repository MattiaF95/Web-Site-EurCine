import { ChangeDetectionStrategy, Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Router } from '@angular/router';
import { map } from 'rxjs';
import { SalaViewComponent } from '../../components/sala-view/sala-view.component';
import { ShowcaseCardComponent } from '../../components/showcase-card/showcase-card.component';
import { Sala } from '../../core/model/sala.model';
import { SaleService } from '../../core/service/sale.service';

@Component({
  selector: 'app-sale',
  imports: [ShowcaseCardComponent, SalaViewComponent],
  templateUrl: './sale.component.html',
  styleUrl: './sale.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SaleComponent {
  private readonly saleService = inject(SaleService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly sale = toSignal(this.saleService.getAll(), { initialValue: [] as Sala[] });

  private readonly query = toSignal(
    this.route.queryParamMap.pipe(
      map((params) => ({
        nome: (params.get('nome') ?? '').trim()
      }))
    ),
    { initialValue: { nome: '' } }
  );

  readonly selectedSala = computed(() => {
    const all = this.sale();
    const { nome } = this.query();
    if (!nome) {
      return null;
    }
    return all.find((s) => s.nome.toLowerCase() === nome.toLowerCase()) ?? null;
  });

  readonly hasSelectedSala = computed(() => !!this.selectedSala());

  buildCardDetails(sala: Sala): Array<{ label: string; value: string }> {
    const features = (sala.caratteristicheNomi ?? '')
      .split(',')
      .map((f) => f.trim())
      .filter(Boolean);

    const video = features.find((f) =>
      /proiezione|schermo|screenx|hfr|vision|imax|reald/i.test(f)
    ) ?? '-';

    const audio = features.find((f) =>
      /dolby|dts|auro|surround|thx|acustic/i.test(f)
    ) ?? '-';

    return [
      { label: 'Posti', value: `${sala.postiTotali}` },
      { label: 'Video', value: video },
      { label: 'Audio', value: audio }
    ];
  }

  openSala(nome: string): void {
    void this.router.navigate(['/sale'], { queryParams: { nome } });
  }

  viewAll(): void {
    void this.router.navigate(['/sale']);
  }
}
